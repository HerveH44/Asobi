import React from "react";
import {array, func, object, string} from "prop-types";
import {connect} from "react-redux"
import {Spaced} from "../utils";
import Card from "./Card";
import {getCardsAsMap, MAIN, SIDE} from "../../reducers/playerState";
import {onClickZone} from "../../actions/game";

const ZonesAsColumns = ({toCardsMap, onClickZone}) => (
    [MAIN, SIDE].map((zoneName, i) =>
        <CardsZone key={i}
                   zoneName={zoneName}
                   cardsMap={toCardsMap(zoneName)}
                   onClickCard={(card) => () => onClickZone({zone: zoneName, card})}/>)
);

ZonesAsColumns.propTypes = {
    toCardsMap: func.isRequired,
    onClickZone: func.isRequired
};

const CardsZone = ({zoneName, cardsMap, onClickCard}) => {
    let sum = 0;
    let cols = [];

    for (let key in cardsMap) {
        const cards = cardsMap[key].map((card, i) =>
            <Card key={i} card={card} onClick={onClickCard(card)}/>
        );

        sum += cards.length;
        cols.push(<CardsColumn cards={cards} keyTitle={key} key={key} />);
    }

    return (
        <div className='zone'>
            <h1>
                <Spaced elements={[zoneName, sum]}/>
            </h1>
            {cols}
        </div>
    );
};

CardsZone.propTypes = {
    zoneName: string.isRequired,
    cardsMap: object.isRequired,
    onClickCard: func.isRequired
};

const CardsColumn = ({ cards, keyTitle }) => (
    <div className='col'>
        <div>
            {`${keyTitle} - ${cards.length} cards`}
        </div>
        {cards}
    </div>
);

CardsColumn.propTypes = {
    cards: array.isRequired,
    keyTitle: string.isRequired,
};


const mapStateToProps = ({playerState, gameSettings}) => ({
    toCardsMap: (zone) => getCardsAsMap(playerState, zone, gameSettings.sort)
});

const mapDispatchToProps = {
    onClickZone
};

export default connect(mapStateToProps, mapDispatchToProps)(ZonesAsColumns);
