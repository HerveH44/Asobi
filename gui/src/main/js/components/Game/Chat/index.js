import React, {Component} from "react";
import {array, bool, func, object} from "prop-types";
import {pressKeyDown} from "../../../actions/game";
import {connect} from "react-redux";
import * as ReactDOM from "react-dom";

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
    onKeyDown: pressKeyDown
};

export default connect(mapStateToProps, mapDispatchToProps)(Chat);
