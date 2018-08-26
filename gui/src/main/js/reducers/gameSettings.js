import {handleActions} from 'redux-actions';
import {GAME_STATE, leaveGame, PACK_SRV} from "../actions/server"
import {editGameSettings} from "../actions/game"
import {
    changeBeep,
    changeColumnView,
    changeFileName,
    changeFileType,
    changePicksToSB,
    changeSort
} from "../actions/game";
import beepSound from "../../resources/media/beep.wav"

const InitialState = {
    fileTypes: ["cod", "json", "txt"],
    fileType: "txt",
    fileName: "fileName",
    beep: false,
    columnView: false,
    sort: "cmc",
    sortTypes: ["cmc", "color", "type", "rarity"],
    addPicksToSB: false,
};

export default handleActions({
    [PACK_SRV](state) {
        if (state.beep) {
            const audio = new Audio(beepSound);
            audio.play();
        }
        return state;
    },
    [editGameSettings](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
    [changeFileType](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            fileType: event.target.value,
        }
    },
    [changeFileName](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            fileName: event.target.value,
        }
    },
    [changeBeep](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            beep: event.target.checked,
        }
    },
    [changeColumnView](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            columnView: event.target.checked,
        }
    },
    [changeSort](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            sort: event.target.value,
        }
    },
    [changePicksToSB](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            addPicksToSB: event.target.checked,
        }
    },
    [leaveGame](state) {
        state.fileName = "fileName";
        return {
            ...state
        }
    },
    [GAME_STATE](state, {payload}) {
        if (state.fileName === "fileName") {
            const {gameMode, gameType, createdDate} = payload;
            state.fileName = `${gameMode}-${gameType}-${new Date(createdDate).toISOString().slice(0, -5).replace(/-/g, "").replace(/:/g, "").replace("T", "-")}`
        }

        return {
            ...state,
        }
    }
}, InitialState);
