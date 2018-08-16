import {handleActions} from 'redux-actions';
import {onEditStartPanel} from "../actions/game";

const InitialState = {
    addBots: true,
    useTimer: true,
    shufflePlayers: true,
    timers: ["Fast", "Moderate", "Slow", "Leisurely"],
    timerLength: "Moderate",
};

export default handleActions({
    [onEditStartPanel](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
}, InitialState);
