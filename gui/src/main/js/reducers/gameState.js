import {handleActions} from 'redux-actions';
import {GAME_STATE} from "../actions/server"

const InitialState = {
    playersStates: [{
        isBot: false,
        name: "",
        packs: 0,
        time: 0,
        hash: ""
    }],
    self: 0, // TODO
    isHost: true, // TODO
    didGameStart: false,
    gameId: "",
    title: "",
    seats: 0,
    isPrivate: true,
    gameMode: "",
    gameType: "",
    status: "",
    createdDate: "",
    round: 0
};

export default handleActions({
    [GAME_STATE](state, {payload}) {
        return {
            ...state,
            ...payload
        }
    }
}, InitialState);
