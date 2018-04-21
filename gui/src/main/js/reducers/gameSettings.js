import {handleActions} from 'redux-actions';
import {joinGame, editGameSettings} from "../actions/server"

const InitialState = {
    id: "1",
    name: "dr4ft",
    gameId: null,
    type: "draft",
    packsInfo: "some packs infos",
    isHost: true,
    didGameStart: true,
    addBots: true,
    useTimer: true,
    shufflePlayers: true
};

export default handleActions({
    [editGameSettings](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
    [joinGame] (state, {payload}) {
        console.log("join game " + payload)
        return {
            ...state,
            gameId: payload
        }
    },
    // "JOIN_GAME" (state, {payload}) {
    //     return {
    //         ...state,
    //         gameId: payload
    //     }
    // },
    "POOL"(state, {payload}) {
        console.log(payload);
    }
}, InitialState);
