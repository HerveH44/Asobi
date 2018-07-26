import {createAction} from 'redux-actions';

export const onSwap = createAction("SWAP");
export const onKick = createAction("KICK");
export const onChangeLand = createAction("CHANGE_LAND");
export const onChangeDeckSize = createAction("CHANGE_DECKSIZE");
export const onSuggestLands = createAction("SUGGEST_LANDS");
export const onResetLands = createAction("RESET_LANDS");

