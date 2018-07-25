import React from 'react'
import Lobby from "../containers/Lobby";
import Game from "../containers/Game";
import {Route, Switch} from "react-router-dom";
import {connect} from 'react-redux';
import {bool} from "prop-types";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";

const App = ({connected}) => (
    !connected
        ? <div>You are not connected!</div>
        :
        <ConnectedRouter history={history}>
            <Switch>
                <Route path="/games/:gameId" exact component={Game}/>
                <Route path="/" exact component={Lobby}/>
            </Switch>
        </ConnectedRouter>
);

App.propTypes = {
    connected: bool.isRequired
};

const mapStateToProps = ({websocket: {
        connected
    }}) => {
    return {connected}
};

export default connect(mapStateToProps)(App);
