import {createStore as reduxCreateStore} from "redux";
import {composeWithDevTools} from 'redux-devtools-extension';
import reducers from "../reducers";
import middlewares from "../middlewares";
import {persistStore, persistReducer} from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const persistConfig = {
    key: 'root',
    storage
}
const persistedReducer = persistReducer(persistConfig, reducers)

export default() => {
    let store = reduxCreateStore(persistedReducer, composeWithDevTools(middlewares))
    let persistor = persistStore(store)
    return {store, persistor}
}
