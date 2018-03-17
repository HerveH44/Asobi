import { createStore as reduxCreateStore, applyMiddleware } from "redux";

/*
 * action types
 */

// const SET_NAVIGATOR_POSITION = "SET_NAVIGATOR_POSITION";

/*
 * action creators
 */

// export function setNavigatorPosition(val) {
//   return { type: SET_NAVIGATOR_POSITION, val };
// }

/*
 * reducer
 */
const reducer = (state, action) => {
  switch (action.type) {
    default:
      return state;
  }
};

const initialState = {
  siteTitle: "www.dr4ft.info",
  err: "",
  numUsers: 0,
  numPlayers: 0,
  numActiveGames: 0,
  roomInfo : [],
  version: "",
  motd: []
};

const createStore = () =>
  reduxCreateStore(reducer, initialState, applyMiddleware());

export default createStore;
