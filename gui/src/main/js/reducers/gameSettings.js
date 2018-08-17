import {handleActions} from 'redux-actions';
import {editGameSettings, GAME_STATE, leaveGame, onNewPack} from "../actions/server"
import {
    onChangeBeep,
    onChangeColumnView,
    onChangeFileName,
    onChangeFileType,
    onChangePicksToSB,
    onChangeSort
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
    [onNewPack](state) {
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
    [onChangeFileType](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            fileType: event.target.value,
        }
    },
    [onChangeFileName](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            fileName: event.target.value,
        }
    },
    [onChangeBeep](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            beep: event.target.checked,
        }
    },
    [onChangeColumnView](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            columnView: event.target.checked,
        }
    },
    [onChangeSort](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            sort: event.target.value,
        }
    },
    [onChangePicksToSB](state, {payload: event}) {
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
