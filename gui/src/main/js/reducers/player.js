import {handleActions} from 'redux-actions';
import {PLAYER_ID} from "../actions/server"

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
    }
}, InitialState);
