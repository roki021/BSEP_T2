import random
from enum import Enum


class Scale(Enum):
    ZERO = 0
    LOW = 1
    MEDIUM_LOW = 2
    MEDIUM = 3
    MEDIUM_HIGH = 4
    HIGH = 5


def pulse_state_machine(state):
    bound = (70, 80)
    if state == Scale.ZERO:
        bound = (0, 30)
    elif state == Scale.LOW:
        bound = (30, 40)
    elif state == Scale.MEDIUM_LOW:
        bound = (40, 60)
    elif state == Scale.MEDIUM:
        bound = (60, 80)
    elif state == Scale.MEDIUM_HIGH:
        bound = (80, 100)
    elif state == Scale.HIGH:
        bound = (100, 150)
    
    return random.randint(bound[0], bound[1])


def pressure_low_state_machine(state):
    bound = (80, 90)
    if state == Scale.LOW:
        bound = (30, 50)
    elif state == Scale.MEDIUM_LOW:
        bound = (50, 70)
    elif state == Scale.MEDIUM:
        bound = (70, 80)
    elif state == Scale.MEDIUM_HIGH:
        bound = (80, 100)
    elif state == Scale.HIGH:
        bound = (100, 160)
    
    return random.randint(bound[0], bound[1])


def pressure_high_state_machine(state):
    bound = (110, 120)
    if state == Scale.LOW:
        bound = (60, 80)
    elif state == Scale.MEDIUM_LOW:
        bound = (80, 110)
    elif state == Scale.MEDIUM:
        bound = (110, 120)
    elif state == Scale.MEDIUM_HIGH:
        bound = (120, 130)
    elif state == Scale.HIGH:
        bound = (130, 200)
    
    return random.randint(bound[0], bound[1])


def temp_state_machine(state):
    bound = (36, 37)
    if state == Scale.LOW or state == Scale.LOW:
        bound = (35, 36)
    elif state == Scale.MEDIUM:
        bound = (36, 37)
    elif state == Scale.MEDIUM_HIGH:
        bound = (37, 38)
    elif state == Scale.HIGH:
        bound = (38, 41)
    
    return round(random.uniform(bound[0], bound[1]), 2)