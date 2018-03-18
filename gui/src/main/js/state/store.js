import { createStore as reduxCreateStore, applyMiddleware } from "redux";
import websocket, {WEBSOCKET_SEND} from "redux-middleware-websocket";
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
    case WEBSOCKET_SEND:
      console.log("sending " + action.payload);
      return state;
    default:
      return state
  }
};

const initialState = {
  siteTitle: "www.dr4ft.info",
  err: "",
  numUsers: 0,
  numPlayers: 0,
  numActiveGames: 0,
  roomsInfo: [],
  motd: ["test motd"],
  repo: "https://github.com/herveh44/asobi",
  version: "FixMe"
};

const createStore = () =>
  reduxCreateStore(reducer, initialState, applyMiddleware(websocket));

export default createStore;
