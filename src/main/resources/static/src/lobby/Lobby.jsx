import React, {Component} from "react";

import App from "Src/app";

import Header from "./Header";
import JoinPanel from "./JoinPanel";
import NewsPanel from "./NewsPanel";
import CreatePanel from "./CreatePanel";
import Version from "./Version";
import Footer from "./Footer";

export default class Lobby extends Component {
    componentDidMount() {
        App.register(this);
    }

    render() {
        const {roomInfo, VERSION, MOTD} = App.state;
        document.title = App.state.SITE_TITLE;

        return (
            <div className="container">
                <div className="lobby">
                    <Header/>
                    <CreatePanel/>
                    <JoinPanel roomInfo={roomInfo}/>
                    <NewsPanel motd={MOTD}/>
                    <Footer/>
                    <Version version={VERSION}/>
                </div>
            </div>
        );
    }
}
