import {handleActions} from 'redux-actions';
import {editGameSettings, GAME_ID, joinGame, onNewPack} from "../actions/server"
import {
    onChangeBeep,
    onChangeColumnView,
    onChangeFileName,
    onChangeFileType, onChangePicksToSB,
    onChangeSort,
    onToggleChat
} from "../actions/game";
import beepSound from "../../resources/media/beep.wav"
import {MAIN, SIDE} from "./playerState";

const InitialState = {
    gameId: null,
    authToken: null,
    type: "draft",
    packsInfo: "some packs infos",
    addBots: true,
    useTimer: true,
    shufflePlayers: true,
    timers: ["Fast", "Moderate", "Slow", "Leisurely"],
    timerLength: "Moderate",
    fileTypes: ["cod", "json", "txt"],
    fileType: "txt",
    fileName: "fileName",
    showChat: true,
    beep: false,
    columnView: false,
    sort: "cmc",
    sortTypes: ["cmc", "color", "type", "rarity"],
    addPicksToSB: false,
};

export default handleActions({
    [onNewPack](state) {
        if(state.beep) {
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
    [onToggleChat](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            showChat: event.target.checked,
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
}, InitialState);
