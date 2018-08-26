import {handleActions} from 'redux-actions';
import {editStartPanel} from "../actions/game";

const InitialState = {
    addBots: true,
    useTimer: true,
    shufflePlayers: true,
    timers: ["Fast", "Moderate", "Slow", "Leisurely"],
    timerLength: "Moderate",
};

export default handleActions({
    [editStartPanel](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
}, InitialState);
