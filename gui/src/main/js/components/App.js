import React from 'react'
import Lobby from "../containers/Lobby";
import Game from "./Game";
import {Route, Switch, Redirect} from "react-router-dom";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";

const App = () => (
    <ConnectedRouter history={history}>
        <Switch>
            <Route path="/games/:id" exact component={Game}/>
            <Route path="/" exact component={Lobby}/>
        </Switch>
    </ConnectedRouter>
);

export default App;
