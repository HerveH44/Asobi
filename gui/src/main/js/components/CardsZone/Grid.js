import React from "react";
import {array, func, string} from "prop-types";
import uniqueId from "lodash.uniqueid";
import Card from "./Card";
import {Spaced} from "../utils";

const Grid = ({zoneName, zoneTitle, zoneSubtitle, cards, pick, autoPick, autoPickId}) => (
    <div className='zone' key={uniqueId()}>
        <h1>
            <Spaced elements={[zoneTitle, zoneSubtitle]}/>
        </h1>
        {cards.map(card => <Card key={uniqueId()}
                                 card={card}
                                 zoneName={zoneName}
                                 pick={pick}
                                 autoPick={autoPick}
                                 autoPickId={autoPickId}
        />)}
    </div>
);

Grid.propTypes = {
    zoneName: string.isRequired,
    zoneTitle: string.isRequired,
    zoneSubtitle: string.isRequired,
    cards: array.isRequired,
    pick: func.isRequired,
    autoPick: func.isRequired,
    autoPickId: string.isRequired
};

export default Grid;
