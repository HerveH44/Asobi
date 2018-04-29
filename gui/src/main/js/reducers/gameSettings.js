import {handleActions} from 'redux-actions';
import {joinGame, editGameSettings, GAME_ID, PLAYER_ID} from "../actions/server"

const InitialState = {
    id: "1",
    name: "dr4ft",
    gameId: null,
    playerId: null,
    authToken: null,
    type: "draft",
    packsInfo: "some packs infos",
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
    [joinGame](state, {payload}) {
        console.log("join game " + payload);
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
    [PLAYER_ID](state, {payload: playerId}) {
        console.log("player id !")
        return {
            ...state,
            playerId
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
