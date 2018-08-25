import {handleActions} from 'redux-actions';
import {autoPick, leaveGame, PACK_SRV, PICKED_CARD, RECONNECT, pick} from "../actions/server"
import {
    changeDeckSize,
    changeLand,
    changePicksToSB,
    clickCardZone,
    resetLands,
    suggestLands,
    clickCopyToClipboard
} from "../actions/game";
import _ from "../lib/utils";

export const PACK = "Pack";
export const MAIN = "Main";
export const SIDE = "Side";
export const JUNK = "Junk";

export const CARDS = {
    Plains: 401994,
    Island: 401927,
    Swamp: 402059,
    Mountain: 401961,
    Forest: 401886
};

const InitialState = {
    [PACK]: [],
    [MAIN]: [],
    [SIDE]: [],
    [JUNK]: [],
    autoPickId: "",
    pickNumber: 0,
    deckSize: 40,
    addPicksToSB: false,
};

export default handleActions({
    [clickCopyToClipboard](state, {payload}) {

        //Copy deck to clipboard
        let textField = document.createElement("textarea");
        textField.value = payload;
        document.body.appendChild(textField);
        textField.select();
        document.execCommand("copy");
        textField.remove();

        return state;
    },
    [clickCardZone](state, {payload: {zone, card}}) {
        const newZone = zone !== MAIN ? MAIN : SIDE;

        //Add card to the new Zone
        state[newZone].push(card);

        //Remove card from the current zone
        const cardIndex = state[zone].map(({id}) => id).indexOf(card.id);
        if (cardIndex !== -1) {
            state[zone].splice(cardIndex, 1);
        }

        return {
            ...state,
        }
    },
    [RECONNECT](state, {payload}) {
        const zone = state.addPicksToSB ? SIDE : MAIN;
        return {
            ...state,
            [zone]: payload
        }

    },
    [PACK_SRV](state, {payload}) {
        return {
            ...state,
            [PACK]: payload.cards,
            pickNumber: payload.pickNumber
        }
    },
    [PICKED_CARD](state, {payload}) {
        const zone = state.addPicksToSB ? SIDE : MAIN;
        return {
            ...state,
            autoPickId: "",
            [zone]: state[zone].concat([payload])
        }
    },
    [pick](state) {
        return {
            ...state,
            [PACK]: []
        }
    },
    [autoPick](state, {payload}) {
        return {
            ...state,
            autoPickId: payload
        }
    },
    [leaveGame](state) {
        return {
            ...state,
            [PACK]: [],
            [MAIN]: [],
            [SIDE]: [],
            [JUNK]: []
        };
    },
    [changeLand](state, {payload: {zoneName, cardName, event}}) {
        event.persist();
        let zone = state[zoneName];
        const diff = event.target.value - zone.filter(({name}) => name === cardName).length;

        switch (true) {
            case diff > 0:
                for (let i = 0; i < diff; i++) {
                    zone.push(makeBasicLand(cardName))
                }
                break;

            case diff < 0:
                for (let i = diff; i < 0; i++) {
                    const index = zone.map(({name}) => name).indexOf(cardName);
                    zone.splice(index, 1);
                }
                break;
        }

        return {
            ...state
        }
    },
    [resetLands](state) {
        return {
            ...state,
            [MAIN]: state[MAIN].filter(({name}) => !Object.keys(CARDS).includes(name)),
            [SIDE]: state[SIDE].filter(({name}) => !Object.keys(CARDS).includes(name)),
        };
    },
    [changeDeckSize](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            deckSize: +event.target.value
        }
    },
    [changePicksToSB](state, {payload: event}) {
        event.persist();
        return {
            ...state,
            addPicksToSB: event.target.checked,
            [MAIN]: [],
            [SIDE]: state[MAIN].concat(state[SIDE])
        }
    },
    [suggestLands](state) {

        //Reset Lands
        state[MAIN] = state[MAIN].filter(({name}) => !Object.keys(CARDS).includes(name));
        state[SIDE] = state[SIDE].filter(({name}) => !Object.keys(CARDS).includes(name));

        // Algorithm: count the number of mana symbols appearing in the costs of
        // the cards in the pool, then assign lands roughly commensurately.
        const colors = ["W", "U", "B", "R", "G"];
        const colorRegex = /{[^}]+}/g;
        const manaSymbols = {};
        colors.forEach(x => manaSymbols[x] = 0);

        // Count the number of mana symbols of each type.
        for (let card of state[MAIN]) {
            if (!card.manaCost)
                continue;
            let cardManaSymbols = card.manaCost.match(colorRegex);

            for (let color of colors)
                for (let symbol of cardManaSymbols)
                    // Test to see if '{U}' contains 'U'. This also handles things like
                    // '{G/U}' triggering both 'G' and 'U'.
                    if (symbol.indexOf(color) !== -1)
                        manaSymbols[color] += 1;
        }

        // Round-robin choose the lands to go into the deck. For example, if the
        // mana symbol counts are W: 2, U: 2, B: 1, cycle through the sequence
        // [Plains, Island, Swamp, Plains, Island] infinitely until the deck is
        // finished.
        //
        // This has a few nice effects:
        //
        //   * Colors with greater mana symbol counts get more lands.
        //
        //   * When in a typical two color deck adding 17 lands, the 9/8 split will
        //   be in favor of the color with slightly more mana symbols of that
        //   color.
        //
        //   * Every color in the deck is represented, if it is possible to do so
        //   in the remaining number of cards.
        //
        //   * Because of the minimum mana symbol count for each represented color,
        //   splashing cards doesn't add exactly one land of the given type
        //   (although the land count may still be low for that color).
        //
        //   * The problem of deciding how to round land counts is now easy to
        //   solve.
        let manaSymbolsToAdd = colors.map(color => manaSymbols[color]);
        let colorsToAdd = [];
        const emptyManaSymbols = () => !manaSymbolsToAdd.every(x => x === 0);

        for (let i = 0; emptyManaSymbols(); i = (i + 1) % colors.length) {
            if (manaSymbolsToAdd[i] === 0)
                continue;
            colorsToAdd.push(colors[i]);
            manaSymbolsToAdd[i]--;
        }

        if (colorsToAdd.length > 0) {
            let mainDeckSize = state[MAIN].length;
            let landsToAdd = state.deckSize - mainDeckSize;

            let j = 0;
            for (let i = 0; i < landsToAdd; i++) {
                const color = colorsToAdd[j];
                const COLORS_TO_LANDS = {
                    "W": "Plains",
                    "U": "Island",
                    "B": "Swamp",
                    "R": "Mountain",
                    "G": "Forest",
                };
                const land = COLORS_TO_LANDS[color];
                state[MAIN].push(makeBasicLand(land));

                j = (j + 1) % colorsToAdd.length;
            }
        }

        return {
            ...state
        }
    },
}, InitialState);

export const getCardsAsMap = (state, zone, sort) => {
    let groups = _.group(state[zone], sort);
    for (let key in groups)
        _.sort(groups[key], sortLandsBeforeNonLands, "color", "cmc", "name");

    let keys = Object.keys(groups);
    let arr;

    switch (sort) {
        case "cmc":
            arr = [];
            for (let key in groups)
                if (parseInt(key) > 6) {
                    [].push.apply(arr, groups[key]);
                    delete groups[key];
                }

            if (arr.length) {
                groups["6"] || (groups["6"] = [])
                ;[].push.apply(groups["6"], arr);
            }
            return groups;

        case "color":
            keys =
                ["Colorless", "White", "Blue", "Black", "Red", "Green", "Multicolor"]
                    .filter(x => keys.indexOf(x) > -1);
            break;
        case "rarity":
            keys =
                ["Basic Land", "Common", "Uncommon", "Rare", "Mythic Rare", "Special"]
                    .filter(x => keys.indexOf(x) > -1);
            break;
        case "type":
            keys = keys.sort();
            break;
    }

    let o = {};
    for (let key of keys)
        o[key] = groups[key];
    return o;
};

function sortLandsBeforeNonLands(lhs, rhs) {
    let isLand = x => x.type.toLowerCase().indexOf("land") !== -1;
    let lhsIsLand = isLand(lhs);
    let rhsIsLand = isLand(rhs);
    return rhsIsLand - lhsIsLand;
}

export const getCardsAsArray = (state, zone, sort) => {
    let groups = _.group(state[zone], sort);
    for (let key in groups)
        _.sort(groups[key], sortLandsBeforeNonLands, "color", "cmc", "name");

    let keys = Object.keys(groups);
    let arr;
    switch (sort) {
        case "cmc":
            arr = [];
            for (let key in groups)
                arr[parseInt(key)] = groups[key];
            return arr.reduce((acc, val) => acc.concat(val), []);
        case "color":
            keys =
                ["Colorless", "White", "Blue", "Black", "Red", "Green", "Multicolor"]
                    .filter(x => keys.indexOf(x) > -1);
            break;
        case "rarity":
            keys =
                ["Basic Land", "Common", "Uncommon", "Rare", "Mythic Rare", "Special"]
                    .filter(x => keys.indexOf(x) > -1);
            break;
        case "type":
            keys = keys.sort();
            break;
    }

    let o = [];
    for (let key of keys) {
        o.push(...groups[key]);
    }
    return o;
};

export const getDeckInTxt = (state) => {
    let main = {};
    state[MAIN].forEach(({name}) => {
        main[name] = main[name] ? main[name] + 1 : 1;
    });

    let side = {};
    state[SIDE].forEach(({name}) => {
        side[name] = side[name] ? side[name] + 1 : 1;
    });

    return Object.entries(main)
            .map(([name, number]) => `${number} ${name}`)
            .join("\n") + "\nSideboard\n" +
        Object.entries(side)
            .map(([name, number]) => `${number} ${name}`)
            .join("\n");
};

export const downloadDeck = (state, fileName, fileType) => () => {
    let data = "";
    switch (fileType) {
        case "cod":
            data = `\
                <?xml version="1.0" encoding="UTF-8"?>
                <cockatrice_deck version="1">
                  <deckname>${fileName}</deckname>
                  <zone name="main">
                ${codify(state[MAIN])}
                  </zone>
                  <zone name="side">
                ${codify(state[SIDE])}
                  </zone>
                </cockatrice_deck>`;
            break;
        case "json":
            data = JSON.stringify({main: state[MAIN], side: state[SIDE]}, null, 2);
            break;

        case "txt":
            data = getDeckInTxt(state);
            break;
    }

    let link = document.createElement('a');
    link.download = fileName + "." + fileType;
    link.href = `data:,${encodeURIComponent(data)}`;
    document.body.appendChild(link);
    link.click();
    link.remove();
};

const codify = (zone) => {
    const reduced = zone.reduce((acc, {name}) => {
        acc[name] = (acc[name] || 0) + 1;
        return acc;
    }, {});

    return Object.entries(reduced)
        .map(([name, number]) => `    <card number="${number}" name="${name}"/>`)
        .join("\n");
};

const makeBasicLand = (land) => ({
    name: land,
    multiverseid: CARDS[land],
    rarity: "Basic Land",
    cmc: 0,
    color: "Colorless",
    type: "Basic Land"
});
