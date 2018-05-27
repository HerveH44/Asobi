import {handleActions} from 'redux-actions';
import {joinGame, editGameSettings, GAME_ID} from "../actions/server"

const InitialState = {
    gameId: null,
    authToken: null,
    type: "draft",
    packsInfo: "some packs infos",
    addBots: true,
    useTimer: true,
    shufflePlayers: true,
    timers: ["Fast", "Moderate", "Slow", "Leisurely"],
    timerLength: "Moderate"
};

export default handleActions({
    [editGameSettings](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
    [joinGame](state, {payload}) {
        return {
            ...state,
            gameId: payload
        }
    },
    [GAME_ID](state, {payload}) {
        return {
            ...state,
            ...payload
        }
    },
    "POOL"(state, {payload}) {
        console.log(payload);
    }
}, InitialState);
