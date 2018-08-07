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

const ImageHelper = ({onMouseEnter, className, card}) => {
    const src = (url) => `http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=${url}&type=card`;
    const samePicture = !card ? false : card.multiverseid === card.flipMultiverseid; // to flip Kamigawa's cards

    return (
        card
            ? card.doubleFace
            ? <div className={className} id="doubleimg">
                <img src={src(card.multiverseid)} onMouseEnter={onMouseEnter(card)} />
                <img src={src(card.flipMultiverseid)} onMouseEnter={onMouseEnter(card)} style={samePicture? {"transform":"rotate(180deg)"}: {}}/>
            </div>
            : <img className={className}
                   id='img'
                   onMouseEnter={onMouseEnter(card)}
                   src={src(card.multiverseid)}/>
            : <div />
    );
};


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
            <Card key={card.id + i} card={card}
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
