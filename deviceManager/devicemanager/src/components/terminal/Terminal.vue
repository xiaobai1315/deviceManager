<template>
  <div class="container-box">
    <div class="container-header">
      <h2>命令行</h2>
    </div>
    <section>
      <div id="log" style="margin-top:20px;">
        <div class="terminal" id="terminal"></div>
      </div>
    </section>
  </div>
</template>
<script>
import { Terminal } from "xterm";
import { FitAddon } from "xterm-addon-fit";
export default {
  name: "Xterm",
  data() {
    return {
      term: null,
      socket: null,
      rows: 40,
      SetOut: false,
      isKey: false,
      id: "",
      WebSocketUrl: "", // 链接后台的地址
      firstConnect: false,
      firstBash: "\r\n $",
      getMsg: "",
    };
  },
  created() {  
    let url = "ws://127.0.0.1:8081/websocket/test"; 
    // 初始化sorket对象
    this.init(url);     
  },
  beforeDestroy() {
    console.log("beforeDestroy...")
    this.socket.close();
    this.term.dispose();
  },
  methods: {
    //Xterm主题
    initXterm() {
      const term = new Terminal({
        rendererType: "canvas", //渲染类型
        pid: 1,
        name: 'terminal',
        rows: parseInt(30), //行数
        cols: parseInt(50),
        convertEol: true, //启用时，光标将设置为下一行的开头
        scrollback: 500,//终端中的回滚量
        disableStdin: false, //是否应禁用输入。
        cursorStyle: 'underline', //光标样式
        cursorBlink: true, //光标闪烁
        tabStopWidth: 8, //制表宽度
        screenKeys: true, //Xterm下的快捷键
        theme: {
            foreground: '#58a6ff', //字体,LightGreen,Orange,SlateBlue,Magenta Purple Red Violet White Yellow
            background: '#2B2B2B', //背景色
            // foreground: "#7e9192", //字体
            // background: "#002833", //背景色
            cursor: "Orange", //设置光标
            lineHeight: 16
        }
    });
 
      const fitAddon = new FitAddon();
 
      term.loadAddon(fitAddon);
      term.open(document.getElementById("terminal"));
      fitAddon.fit();
      term.focus();
      let _this = this;
      // 换行并输入起始符“$”
      term.prompt = () => {
        // term.write("\r\n $ ");
        term.write(this.firstBash);
      };
      term.prompt();
      let arr = '';
      // 终端输入字符触发的事件
      term.onData(function(key) {
          console.log('item.ondata- > ' + key)
        _this.firstConnect = false;
        let code = key.charCodeAt(0);
        let order = null;
        
        if (code === 13) {
          // _this.term.write("\r\n $" + _this.firstBash);
          // 输入换行,发送指令给后端服务器，服务器处理后，返回结果
        //   order = {
        //     Op: "stdin",
        //     Data: "\n",
        //     Cols: 140,
        //     Rows: 24,
        //   };

          // 发送指令给服务端
          _this.send(JSON.stringify(arr));

          // 输出数据到终端
        //   _this.term.write(key);
          _this.term.write(_this.firstBash);
          arr = ''
        } else {
        //   order = {
        //     Op: "stdin",
        //     Data: key,
        //     Cols: 140,
        //     Rows: 24,
        //   };
        //   arr.push(JSON.stringify(order));
        //   _this.send(JSON.stringify(arr));
        //   arr.push(key)
          arr += key
          _this.term.write(key);
        }
       
        // console.log(key == "\\n");
        // _this.socket.onmessage = (evt) => {
        //     // 接受服务端返回的数据
        //   if (evt.data.length > 1) {
        //     let str = evt.data.substr(1, evt.data.length - 2);
        //     let arr = JSON.parse(str);
        //     if (code === 13 && _this.firstBash !== JSON.parse(arr[0]).Data) {
        //       console.log('str =>' + str)
        //       _this.term.write(" $" + JSON.parse(arr[0]).Data);
        //       _this.term.write("\r\n $" + _this.firstBash);
        //     }
        //   }
        // };
      });
      _this.term = term;
    },

    init(url) {
      console.log("init websocket")
      // 实例化socket
      this.socket = new WebSocket(url);
      // 监听socket连接
      this.socket.onopen = this.open;
      // 监听socket错误信息
      this.socket.onerror = this.error; 
      // 发送socket消息
      this.socket.onsend = this.send;
      this.socket.onmessage = this.getMessage;
    },

    // 打开socket连接
    open: function() {
      console.log("socket连接成功");
      let order = {
        SessionID: this.id,
        Op: "bind",
      };
      let arr = [];
      arr.push(JSON.stringify(order));
      // 链接成功后发送初始化带有ID信息
      this.firstConnect = true;
      // this.getMessage();
      this.initXterm();
    },

    // socket出错
    error: function() {
      console.log("连接错误");
    },

    // 关闭socket连接
    close: function() {
      this.socket.close();
      console.log("socket已经关闭");
    },

    // 接收从服务端返回的数据
    getMessage: function(msg) {
     
     console.log('reveive message' + msg)
          this.term.write(msg.data);
          this.term.write(this.firstBash);

      // this.socket.onmessage = (evt) => {
      //     console.log('reveive message' + evt)
      //     this.term.write(evt.data);
      //     this.term.write(this.firstBash);
      //   // if (evt.data.length > 1) {
      //   //   let str = evt.data.substr(1, evt.data.length - 2);
      //   //   console.log('str =>' + str)
      //   //   let arr = JSON.parse(str);
      //   //   if (this.firstConnect) {
      //   //     this.term.write(JSON.parse(arr[0]).Data);
      //   //     this.firstBash = JSON.parse(arr[0]).Data;
      //   //   }
      //   // }
      // };
    },

    // 下发数据给服务端
    send: function(order) {
      console.log('send message -> ' + order)
      console.log('socket =>' + this.socket)
      this.socket.send(order);
    },
  },
};
</script>
 
<style scoped>
 /* @import "../../styles/common.less"; */
@import "xterm/css/xterm.css";
</style>