import React, {Fragment} from 'react'
import Lobby from "./Lobby";
import Game from "./Game";
import {Route, Switch} from "react-router-dom";
import {bool} from "prop-types";
import {ConnectedRouter} from 'react-router-redux';
import history from "../state/history";
import {connect} from "react-redux";
import CssBaseline from "@material-ui/core/CssBaseline/CssBaseline";

const App = ({connecting}) => (
    <Fragment>
        <CssBaseline/>
        <ConnectedRouter history={history}>
            {connecting ?
                <div>Loading...</div>
                : <Switch>
                    <Route path="/games/:gameId" exact component={Game}/>
                    <Route path="/" component={Lobby}/>
                </Switch>}
        </ConnectedRouter>
    </Fragment>
);

App.propTypes = {
    connecting: bool.isRequired
};

const mapStateToProps = ({websocket}) => ({
    connecting: /*websocket.connecting*/ false
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(App);
