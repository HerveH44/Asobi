import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import App from './components/App'
import createStore from "./state/store"
import {PersistGate} from 'redux-persist/integration/react';
import "../resources/css/style.css";
import {WEBSOCKET_CONNECT} from "./middlewares/stomp";

const {store, persistor} = createStore();
store.dispatch({
    type: WEBSOCKET_CONNECT,
});

render(
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
            <App/>
        </PersistGate>
    </Provider>,
    document.getElementById('root'));

if (module.hot) {
    module
        .hot
        .accept();
}
