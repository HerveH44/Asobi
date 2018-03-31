import { createAction, handleActions } from 'redux-actions';

export const editGame = createAction("EDIT_GAME");
export const CREATE_GAME = createAction("CREATE_GAME");
const IMPORT_SETS = createAction("IMPORT_SETS");

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
  [IMPORT_SETS](state, {payload: availableSets}) {
    return {
      ...state,
      availableSets
    }
  }
}, initialState);
