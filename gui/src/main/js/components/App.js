import React from 'react'
import Lobby from "./Lobby";
import Game from "./Game";
import {Route, Switch} from "react-router-dom";
import {bool} from "prop-types";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";

const App = () => (
    <ConnectedRouter history={history}>
        <Switch>
            <Route path="/games/:gameId" exact component={Game}/>
            <Route path="/" component={Lobby}/>
        </Switch>
    </ConnectedRouter>
);

export default App;
