import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import App from './components/App'
import store from "./state/store"
import {WEBSOCKET_CONNECT} from "redux-middleware-websocket";

const theStore = store();
theStore.dispatch({
    type: WEBSOCKET_CONNECT,
    payload: {
        url: 'ws://localhost:8080/ws'
    }
});

render(
    <Provider store={theStore}>
        <App/>
    </Provider>,
    document.getElementById('root'));

if (module.hot) {
    module
        .hot
        .accept();
}
