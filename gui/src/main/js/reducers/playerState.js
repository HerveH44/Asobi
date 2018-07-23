import {handleActions} from 'redux-actions';
import {PLAYER_STATE} from "../actions/server"

const InitialState = {
    waitingPack: {cards: []},
    pickedCards: [],
    pack: [],
    side: [],
    junk: [],
    autoPickId: ""
};

export default handleActions({
    [PLAYER_STATE](state, {payload}) {
        return {
            ...state,
            ...payload
        }

    },
    ["AUTOPICK"](state, {payload}) {
        return {
            ...state,
            autoPickId: payload
        }
    }
}, InitialState);
