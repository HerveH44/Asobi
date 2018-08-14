import React from "react";
import {array, func, string} from "prop-types";
import Card from "./Card";
import {Spaced} from "../../utils";

const Grid = ({zoneTitle, zoneSubtitle, cards, addCardTitle= () => {}, addCardClassNames= () => {}, onClickCard }) => (
    <div className='zone'>
        <h1>
            <Spaced elements={[zoneTitle, zoneSubtitle]}/>
        </h1>
        {cards.map((card, index) =>
            <Card key={card.name + index}
                  onClick={onClickCard(card)}
                  classNames={addCardClassNames(card)}
                  title={addCardTitle(card)}
                  card={card}
        />)}
    </div>
);

Grid.propTypes = {
    zoneTitle: string.isRequired,
    zoneSubtitle: string.isRequired,
    cards: array.isRequired,
    addCardTitle: func,
    addCardClassNames: func,
    onClickCard: func.isRequired
};

export default Grid;
