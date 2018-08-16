import React from 'react'
import { render } from 'react-dom'
import { Provider } from 'react-redux'
import App from './components/App'
import createStore from "./state/store"
import { WEBSOCKET_CONNECT } from "redux-middleware-websocket";
import { PersistGate } from 'redux-persist/integration/react';
import SockJS from "sockjs-client";
import "../resources/css/style.css";

const { store, persistor } = createStore();
store.dispatch({
    type: WEBSOCKET_CONNECT,
    payload: {
        websocket: SockJS,
        url: `${location.href}ws`
    }
});

render(
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
            <App />
        </PersistGate>
    </Provider>,
    document.getElementById('root'));

if (module.hot) {
    module
        .hot
        .accept();
}
