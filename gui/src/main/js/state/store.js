import {createStore as reduxCreateStore} from "redux";
import {composeWithDevTools} from 'redux-devtools-extension';
import reducers from "../reducers";
import middlewares from "../middlewares";
import {persistStore, persistReducer} from 'redux-persist'

export default() => {
    let store = reduxCreateStore(reducers, composeWithDevTools(middlewares))
    let persistor = persistStore(store)
    return {store, persistor}
}
