import {createAction} from 'redux-actions';

export const onSwap = createAction("SWAP");
export const onKick = createAction("KICK");
export const onChangeLand = createAction("CHANGE_LAND");
export const onChangeDeckSize = createAction("CHANGE_DECKSIZE");
export const onSuggestLands = createAction("SUGGEST_LANDS");
export const onResetLands = createAction("RESET_LANDS");
export const onClickLog = createAction("CLICK_LOG");
export const onChangeFileType = createAction("CHANGE_FILE_TYPE");
export const onChangeFileName = createAction("CHANGE_FILE_NAME");
export const onToggleChat = createAction("TOGGLE_CHAT");
export const onChangePicksToSB = createAction("CHANGE_PICKS_TO_SB");
export const onChangeBeep = createAction("CHANGE_BEEP");
export const onChangeColumnView = createAction("CHANGE_COLUMN_VIEW");
export const onChangeSort = createAction("CHANGE_SORT");
export const onClickZone = createAction("CLICK_CARD_ZONE");
export const hashDeck = createAction("HASH");
