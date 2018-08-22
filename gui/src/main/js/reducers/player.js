import {handleActions} from 'redux-actions';
import {PLAYER_ID} from "../actions/server";
import {changePlayerName} from "../actions/game"

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
    [changePlayerName](state, {payload}) {
        return {
            ...state,
            name: payload.target.value
        }
    }
}, InitialState);
