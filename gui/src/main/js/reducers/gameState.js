import {handleActions} from 'redux-actions';
import {GAME_STATE} from "../actions/server"

const InitialState = {
    players: [{
        isBot: false,
        name: "",
        packs: 0,
        time: 0,
        hash: ""
    }],
    self: 0,
    isHost: true,
    didGameStart: false
};

export default handleActions({
    [GAME_STATE](state, {payload: players}) {
        return {
            ...state,
            players
        }
    }
}, InitialState);
