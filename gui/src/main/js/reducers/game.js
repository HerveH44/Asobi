import {createAction, handleActions} from 'redux-actions';

export const editGame = createAction("EDIT_GAME");
export const EDIT_PACK_NUMBER = createAction("EDIT_PACK_NUMBER");
export const CREATE_GAME = createAction("CREATE_GAME");
const IMPORT_SETS = createAction("IMPORT_SETS");

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
        [DRAFT]: [
            "XLN", "XLN", "XLN"
        ],
        [SEALED]: [
            "XLN",
            "XLN",
            "XLN",
            "XLN",
            "XLN",
            "XLN"
        ]
    },
    availableSets: {
        expansion: [
            {
                code: "XLN",
                name: "Ixalan"
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
        return {
            ...state,
            availableSets
        }
    },
    [EDIT_PACK_NUMBER](state, {payload: {packsNumber}}) {
        for( let k in state.sets) {
            const setsType = state.sets[k];
            if( setsType.length < packsNumber) {
                const toAdd = packsNumber - setsType.length;
                for(var i = 0; i < toAdd; i++) {
                    setsType.push(setsType.slice(-1)[0]);
                }
            } else if( setsType.length > packsNumber) {
                setsType.splice(packsNumber);
            }
        }

        return {
            ...state,
            packsNumber
        }
    }
}, initialState);
