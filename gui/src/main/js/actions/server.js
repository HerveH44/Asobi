import {createAction} from 'redux-actions';

export const AUTH_TOKEN = createAction("AUTH_TOKEN");
export const PLAYER_ID = createAction("PLAYER_ID");
export const onReconnect = createAction("RECONNECT");
export const onNewPack = createAction("PACK");
export const onPickedCard = createAction("PICKED_CARD");
export const GAME_STATE = createAction("GAME_STATE");
export const joinGame = createAction("JOIN_GAME");
export const leaveGame = createAction("LEAVE_GAME");
export const startGame = createAction("START_GAME");
export const editGameSettings = createAction("EDIT_GAME_SETTINGS");
export const pick = createAction("PICK");
export const autoPick = createAction("AUTOPICK");
export const onLobbyStats = createAction("LOBBY_STATS");
export const onSetPlayerName = createAction("SET_NAME");
export const onGetDraftLog = createAction("DRAFT_LOG");
