import React, {Component} from "react";
import {array, bool, func, object} from "prop-types";
import {onKeyDown} from "../../../actions/game";
import {connect} from "react-redux";
import * as ReactDOM from "react-dom";

// class Chat extends Component {
//     constructor(props) {
//         super(props);
//         this.state = {
//             messages: []
//         };
//     }
//
//     componentDidMount() {
//         App.on("hear", this.hear.bind(this));
//         App.on("chat", messages => this.setState({messages}));
//     }
//
//     componentWillUnmount() {
//         App.off("hear");
//         App.off("chat");
//     }
//
//     render() {
//         // must be mounted to receive messages
//         return (
//             <div className={`chat-container ${App.state.chat ? "" : "chat-container-hidden"}`}>
//                 <div className='chat'>
//                     <div className='messages' ref={(ref) => this.messageElement = ref}>
//                         {this.state.messages.map(this.Message)}
//                     </div>
//                     {this.Entry()}
//                 </div>
//             </div>
//         );
//     }
//
//     hear(msg) {
//         this.state.messages.push(msg);
//         this.forceUpdate(this.scrollChat);
//     }
//
//     scrollChat() {
//         let el = this.messageElement;
//         el.scrollTop = el.scrollHeight;
//     }
//
//     Message(msg) {
//         if (!msg)
//             return;
//
//         let {time, name, text} = msg;
//         let date = new Date(time);
//         let hours = _.pad(2, "0", date.getHours());
//         let minutes = _.pad(2, "0", date.getMinutes());
//         time = `${hours}:${minutes}`;
//
//         return (
//             <div key={_.uid()}>
//                 <time>{time}</time>
//                 {" "}
//                 <span className='name'>{name}</span>
//                 {" "}
//                 {text}
//             </div>
//         );
//     }
//
//     Entry() {
//         return <input autoFocus className='chat-input' type='text' onKeyDown={this.key.bind(this)}
//                       placeholder='/nick name'/>;
//     }
//
//     key(e) {
//         if (e.key !== "Enter")
//             return;
//
//         let el = e.target;
//         let text = el.value.trim();
//         el.value = "";
//
//         if (!text)
//             return;
//
//         if (text[0] === "/")
//             this.command(text.slice(1));
//         else
//             App.send("say", text);
//     }
//
//     command(raw) {
//         let [, command, arg] = raw.match(/(\w*)\s*(.*)/);
//         arg = arg.trim();
//         let text, name;
//
//         switch (command) {
//             case "name":
//             case "nick":
//                 name = arg.slice(0, 15);
//
//                 if (!name) {
//                     text = "enter a name";
//                     break;
//                 }
//
//                 text = `hello, ${name}`;
//                 App.save("name", name);
//                 App.send("name", name);
//                 break;
//             default:
//                 text = `unsupported command: ${command}`;
//         }
//         this.state.messages.push({
//             text,
//             time: Date.now(),
//             name: ""
//         });
//         this.forceUpdate(this.scrollChat);
//     }
// }

class Chat extends Component {

    static propTypes = {
        showChat: bool.isRequired,
        onKeyDown: func.isRequired,
        messages: array.isRequired,
        errorMessages: array.isRequired
    };

    componentDidUpdate(prevProps, prevState, prevContext) {
        if (this.scrollAtBottom) {
            this.scrollToBottom();
        }
    }

    componentWillUpdate(nextProps, nextState, nextContext) {
        this.messagesChanged = nextProps.messages.length !== this.props.messages.length;
        if (this.messagesChanged) {
            const {messagesEl} = this;
            const scrollPos = messagesEl.scrollTop;
            const scrollBottom = (messagesEl.scrollHeight - messagesEl.clientHeight);
            this.scrollAtBottom = (scrollBottom <= 0) || (scrollPos === scrollBottom);
        }
    }

    scrollToBottom = () => {
        const {messagesEl} = this;
        const scrollHeight = messagesEl.scrollHeight;
        const height = messagesEl.clientHeight;
        const maxScrollTop = scrollHeight - height;
        ReactDOM.findDOMNode(messagesEl).scrollTop = maxScrollTop > 0 ? maxScrollTop : 0;
    };

    render() {
        const {showChat, onKeyDown, messages, errorMessages} = this.props;

        return (
            <div className={`chat-container ${showChat ? "" : "chat-container-hidden"}`}>
                <div className='chat'>
                    <div className='messages' ref={re => this.messagesEl = re}>
                        {messages.concat(errorMessages)
                            .sort((a, b) => new Date(a.time) - new Date(b.time))
                            .map((message, key) => <Message key={key} message={message}/>)}
                    </div>
                    <Prompt onKeyDown={onKeyDown}/>
                </div>
            </div>
        );
    }
}

const Message = ({message}) => (
    <div>
        <time>{new Date(message.time).toLocaleTimeString()}</time>
        {" "}
        <span className='name'>{message.name}</span>
        {" "}
        {message.text}
    </div>
);

Message.propTypes = {
    message: object.isRequired,
};

const Prompt = ({onKeyDown}) => (
    <input autoFocus className='chat-input' type='text' onKeyDown={onKeyDown} placeholder='/nick name'/>
);

Prompt.propTypes = {
    onKeyDown: func.isRequired,
};


const mapStateToProps = ({chat}) => ({
    ...chat
});

const mapDispatchToProps = {
    onKeyDown
};

export default connect(mapStateToProps, mapDispatchToProps)(Chat);
