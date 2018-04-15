import React from 'react'
import Lobby from "../containers/Lobby";
import Game from "../containers/Game";
import {Route, Switch} from "react-router-dom";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";

const App = () => (
    <ConnectedRouter history={history}>
        <Switch>
            <Route path="/games/:gameId" exact component={Game}/>
            <Route path="/" exact component={Lobby}/>
        </Switch>
    </ConnectedRouter>
);

export default App;
