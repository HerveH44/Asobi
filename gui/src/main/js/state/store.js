import {createStore as reduxCreateStore} from "redux";
import {composeWithDevTools} from 'redux-devtools-extension';
import reducers from "../reducers";
import middlewares from "../middlewares";

const createStore = () => reduxCreateStore(reducers, composeWithDevTools(middlewares));

export default createStore;
