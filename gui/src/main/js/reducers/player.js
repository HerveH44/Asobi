import {handleActions} from 'redux-actions';
import {PLAYER_ID} from "../actions/server";
import {onChangePlayerName} from "../actions/game"

const InitialState = {
    name: "dr4ft",
    playerId: null
};

export default handleActions({
    [PLAYER_ID](state, {payload: playerId}) {
        return {
            ...state,
            playerId
        }
    },
    [onChangePlayerName](state, {payload}) {
        return {
            ...state,
            name: payload.target.value
        }
    }
}, InitialState);
