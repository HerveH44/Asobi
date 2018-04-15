import {handleActions} from 'redux-actions';
import {joinGame, editGameSettings} from "../actions/server"

const InitialState = {
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
        return {
            ...state,
            gameId: payload
        }
    }
}, InitialState);
