import React, {Component} from "react";
import {array, func, object, string} from "prop-types";
import {connect} from "react-redux"
import {Spaced} from "../utils";
import Card from "./Card";
import {getCardsAsMap, MAIN, SIDE} from "../../reducers/playerState";
import {onClickZone} from "../../actions/game";

class ZonesAsColumns extends Component {
    state = {
        className: "right",
        card: undefined
    };

    static propTypes = {
        toCardsMap: func.isRequired,
        onClickZone: func.isRequired
    };

    onMouseEnterCard = (card) => e => {
        let {offsetLeft} = e.target;
        let {clientWidth} = document.documentElement;

        let imgWidth = 240;
        let colWidth = 180;

        let className = offsetLeft + colWidth > clientWidth - imgWidth
            ? "left"
            : "right";

        this.setState({ card, className });
    };

    onMouseLeaveCard = () => {
        this.setState({
            card: undefined
        });
    };

    render () {
        const {toCardsMap, onClickZone} = this.props;
        const {className, card} = this.state;
        return (
            <div>
                {[MAIN, SIDE].map((zoneName, i) =>
                    <CardsZone key={i}
                               zoneName={zoneName}
                               cardsMap={toCardsMap(zoneName)}
                               onMouseEnterCard={this.onMouseEnterCard}
                               onMouseLeaveCard={this.onMouseLeaveCard}
                               onClickCard={(card) => () => onClickZone({zone: zoneName, card})}/>)}
                <ImageHelper className={className} onMouseEnter={this.onMouseEnterCard} card={card}/>
            </div>
        )
    }
}

const ImageHelper = ({onMouseEnter, className, card}) => (
    card
        ? card.isDoubleFaced
        ? <div className={className} id="doubleimg">
            <img className="card" src={card.url} onMouseEnter={onMouseEnter(card)} />
            <img className="card" src={card.flippedCardURL} onMouseEnter={onMouseEnter(card)} />
        </div>
        : <img className={className}
               id='img'
               onMouseEnter={onMouseEnter(card)}
               src={`http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${card.multiverseid}&type=card`}/>
        : <div />
);

ImageHelper.propTypes = {
    onMouseEnter: func.isRequired,
    className: string.isRequired,
    card: object
};

const CardsZone = ({zoneName, cardsMap, onClickCard, onMouseEnterCard, onMouseLeaveCard}) => {
    let sum = 0;
    let cols = [];

    for (let key in cardsMap) {
        const cards = cardsMap[key].map((card, i) =>
            <Card key={i} card={card}
                  onClick={onClickCard(card)}
                  onMouseEnter={onMouseEnterCard(card)}
                  onMouseLeave={onMouseLeaveCard}/>
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
    onClickCard: func.isRequired,
    onMouseEnterCard: func.isRequired,
    onMouseLeaveCard: func.isRequired
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
