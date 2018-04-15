import {createAction} from 'redux-actions';

export const GAME_ID = createAction("GAME_ID");
export const joinGame = createAction("JOIN_GAME");
export const leaveGame = createAction("LEAVE_GAME");
export const startGame = createAction("START_GAME");
export const editGameSettings = createAction("EDIT_GAME");
