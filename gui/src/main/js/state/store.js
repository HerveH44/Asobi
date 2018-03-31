import { createStore as reduxCreateStore } from "redux";
import reducers from "../reducers";
import middlewares from "../middlewares"

const createStore = () =>
  reduxCreateStore(reducers, middlewares);

export default createStore;
