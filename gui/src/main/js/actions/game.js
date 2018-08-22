import {createAction} from 'redux-actions';

/*
    These actions are used to chante state's app.
    Any reducer may use them to change its state.
    for more information: https://github.com/redux-utilities/redux-actions
 */

export const changeLand = createAction("LAND_CHANGE");
export const changeDeckSize = createAction("DECK_SIZE_CHANGE");
export const suggestLands = createAction("LANDS_SUGGEST");
export const resetLands = createAction("LANDS_RESET");
export const changeFileType = createAction("FILE_TYPE_CHANGE");
export const changeFileName = createAction("FILE_NAME_CHANGE");
export const changePicksToSB = createAction("PICKS_TO_SB_CHANGE");
export const changeBeep = createAction("BEEP_CHANGE");
export const changeColumnView = createAction("COLUMN_VIEW_CHANGE");
export const changeSort = createAction("SORT_CHANGE");
export const clickCardZone = createAction("CARD_ZONE_CLICK");
export const clickCopyToClipboard = createAction("CLIPBOARD_COPY");
export const pressKeyDown = createAction("CHAT_KEY_DOWN_PRESS");
export const toggleChat = createAction("CHAT_TOGGLE");
export const errorMessage = createAction("ERROR_MESSAGE");
export const editStartPanel = createAction("START_PANEL_EDIT");
export const changePlayerName = createAction("PLAYER_NAME_CHANGE");
export const editGameSettings = createAction("GAME_SETTINGS_EDIT");
export const editGame = createAction("GAME_EDIT");
export const editPackNumber = createAction("PACK_NUMBER_EDIT");
