import {handleActions} from 'redux-actions';
import {GAME_STATE} from "../actions/server"

const InitialState = {
    players: []
};

export default handleActions({
    [GAME_STATE](state, {payload: players}) {
        return {
            ...state,
            players
        }
    }
}, InitialState);
