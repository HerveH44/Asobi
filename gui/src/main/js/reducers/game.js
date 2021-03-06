import {handleActions} from 'redux-actions';
import {IMPORT_SETS} from "../actions/server";
import {editGame, editPackNumber} from "../actions/game";

const DRAFT = "DRAFT";
const SEALED = "SEALED";
const gameTypes = [DRAFT, SEALED];

export const NORMAL = "NORMAL";
export const CUBE = "CUBE";
export const CHAOS = "CHAOS";
const gameModes = [NORMAL, CUBE, CHAOS];

const initialState = {
    title: "",
    seats: 2,
    packsNumber: 3,
    isPrivate: false,
    gameType: DRAFT,
    gameTypes,
    gameMode: NORMAL,
    gameModes,
    sets: {
        [DRAFT]: [],
        [SEALED]: []
    },
    availableSets: {
        expansion: [
            {
                code: "XLN",
                name: "Ixalan",
                releaseDate: "2000-01-01"
            }
        ]
    },
    cubeList: "",
    modernOnly: false,
    totalChaos: false
};

export default handleActions({
    [editGame](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
    [IMPORT_SETS](state, {payload: availableSets}) {

        const lastExpansion = availableSets["expansion"]
            .reduce((acc, curValue) => {
                return !acc ? curValue
                    : new Date(acc.releaseDate).getTime() > new Date(curValue.releaseDate).getTime()
                        ? acc : curValue;
            });

        if (state.sets[DRAFT].length === 0) {
            state.sets[DRAFT] = new Array(state.packsNumber).fill(lastExpansion.code);
        }

        if (state.sets[SEALED].length === 0) {
            state.sets[SEALED] = new Array(state.packsNumber).fill(lastExpansion.code);
        }

        return {
            ...state,
            availableSets
        }
    },
    [editPackNumber](state, {payload: {packsNumber}}) {
        for (let k in state.sets) {
            const setsType = state.sets[k];
            if (setsType.length < packsNumber) {
                const toAdd = packsNumber - setsType.length;
                for (let i = 0; i < toAdd; i++) {
                    setsType.push(setsType.slice(-1)[0]);
                }
            } else if (setsType.length > packsNumber) {
                setsType.splice(packsNumber);
            }
        }

        return {
            ...state,
            packsNumber
        }
    }
}, initialState);
