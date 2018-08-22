import {createAction} from 'redux-actions';

/*
    These actions are used to communication with the server.
    If you change the string value here, be sure to adapt it on the server site (EventType)
 */

export const AUTH_TOKEN = "AUTH_TOKEN";
export const PLAYER_ID = "PLAYER_ID";
export const RECONNECT = "RECONNECT";
export const DRAFT_LOG = "DRAFT_LOG";
export const LOBBY_STATS = "LOBBY_STATS";
export const PACK_SRV = "PACK";
export const PICKED_CARD = "PICKED_CARD";
export const GAME_STATE = "GAME_STATE";
export const ERROR = "ERROR";
export const IMPORT_SETS = "IMPORT_SETS";

// TODO: use something like payloadCreator ? the second argument of createAction
// see https://redux-actions.js.org/api/createaction#createactiontype-payloadcreator
export const createGame = createAction("GAME_CREATE");
export const startGame = createAction("START_GAME");
export const joinGame = createAction("JOIN_GAME");
export const leaveGame = createAction("LEAVE_GAME");
export const pick = createAction("PICK");
export const autoPick = createAction("AUTOPICK");
export const onSetPlayerName = createAction("SET_NAME");
export const hashDeck = createAction("HASH");
export const onClickLog = createAction("CLICK_LOG");
export const onSwap = createAction("PLAYER_SWAP");
export const onKick = createAction("KICK");
