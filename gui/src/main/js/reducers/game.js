import { createAction, handleActions } from 'redux-actions';

export const editGame = createAction("EDIT_GAME");

const DRAFT = "draft";
const SEALED = "sealed";
const gameTypes = [ DRAFT, SEALED ];

const NORMAL = "normal";
const CUBE = "cube";
const CHAOS = "chaos";
const gameModes = [ NORMAL, CUBE, CHAOS ];

const initialState = {
  title: "",
  seats: 2,
  isPrivate: false,
  gameType: DRAFT,
  gameTypes,
  gameMode: NORMAL,
  gameModes
};

export default handleActions({
  [editGame](state, {payload}) {
    return {
      ...state,
      ...payload
    };
  }
}, initialState);
