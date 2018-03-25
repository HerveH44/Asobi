import { createAction, handleActions } from 'redux-actions';
import {WEBSOCKET_MESSAGE} from "redux-middleware-websocket";

export const editGame = createAction("EDIT_GAME");

const DRAFT = "draft";
const SEALED = "sealed";
const gameTypes = [ DRAFT, SEALED ];

const NORMAL = "normal";
const CUBE = "cube";
const CHAOS = "chaos";
const gameModes = [ NORMAL, CUBE, CHAOS ];

const XLN = {
  code: "XLN",
  title: "Ixalan"
};

const initialState = {
  title: "",
  seats: 2,
  isPrivate: false,
  gameType: DRAFT,
  gameTypes,
  gameMode: NORMAL,
  gameModes,
  sets: {
    [DRAFT]: [XLN, XLN, XLN],
    [SEALED]: [XLN, XLN, XLN, XLN, XLN, XLN]
  },
  availableSets: {
    expansion: [{
      code: "XLN",
      name: "Ixalan"
    }]
  }
};

export default handleActions({
  [editGame](state, {payload}) {
    return {
      ...state,
      ...payload
    };
  },
  [WEBSOCKET_MESSAGE](state, {payload}) {
    return {
      ...state,
      availableSets: JSON.parse(payload.data)
    }
  }
}, initialState);
