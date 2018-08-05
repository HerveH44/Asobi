import {createAction} from 'redux-actions';

export const GAME_ID = createAction("GAME_ID");
export const PLAYER_ID = createAction("PLAYER_ID");
export const onReconnect = createAction("PLAYER_STATE");
export const onNewPack = createAction("PACK");
export const onPickedCard = createAction("PICKED_CARD");
export const GAME_STATE = createAction("GAME_STATE");
export const joinGame = createAction("JOIN_GAME");
export const leaveGame = createAction("LEAVE_GAME");
export const startGame = createAction("START_GAME");
export const editGameSettings = createAction("EDIT_GAME_SETTINGS");
export const pick = createAction("PICK");
export const autoPick = createAction("AUTOPICK");
