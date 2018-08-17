import React from 'react'
import Lobby from "./Lobby";
import Game from "./Game";
import {Route, Switch} from "react-router-dom";
import {bool} from "prop-types";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";
import {connect} from "react-redux";

const App = ({connecting}) => (
    <ConnectedRouter history={history}>
        {connecting ?
            <div>Loading...</div>
            : <Switch>
                <Route path="/games/:gameId" exact component={Game}/>
                <Route path="/" component={Lobby}/>
            </Switch>}
    </ConnectedRouter>
);

App.propTypes = {
    connecting: bool.isRequired
};

const mapStateToProps = ({websocket}) => ({
    connecting: websocket.connecting
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(App);
