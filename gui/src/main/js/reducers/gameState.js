import {handleActions} from 'redux-actions';
import {GAME_STATE} from "../actions/server"

const InitialState = {
    playersStates: [{
        seat: 0,
        isBot: false,
        name: "",
        packs: 0,
        time: 0,
        cockHash: "",
        mwsHash: ""
    }],
    self: 0,
    isHost: true,
    didGameStart: false,
    gameId: "",
    title: "",
    seats: 0,
    isPrivate: true,
    gameMode: "",
    gameType: "",
    status: "",
    createdDate: "",
    round: 0,
};

export default handleActions({
    //TODO: refactor: je veux pas que mes messages arrivent la.
    // Le mieux serait de séparer les infos...
    [GAME_STATE](state, {payload}) {

        return {
            ...state,
            ...payload
        }
    }
}, InitialState);
